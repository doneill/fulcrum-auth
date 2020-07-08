package com.jdoneill.ktmultiplatform.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.jdoneill.api.FulcrumApi

import com.jdoneill.common.FulcrumAuthDriverFactory
import com.jdoneill.common.createDb
import com.jdoneill.db.FulcrumAuth
import com.jdoneill.db.FulcrumAuthModelQueries
import com.jdoneill.ktmultiplatform.R
import com.jdoneill.model.FulcrumAuthenticationResponse

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

import java.util.Base64

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    private lateinit var api: FulcrumApi

    private lateinit var loginButton: Button
    private lateinit var userEmailField: EditText
    private lateinit var userPasswordField: EditText

    private lateinit var fulcrumDb: FulcrumAuth
    private lateinit var fulcrumAuthQuery: FulcrumAuthModelQueries

    override val coroutineContext: CoroutineContext
        get() = job + Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userEmailField = findViewById(R.id.user_email_view)
        userPasswordField = findViewById(R.id.user_password_view)

        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val user = userEmailField.text.toString()
            val password = userPasswordField.text.toString()
            getAccount(user, password)
        }

        val driver = FulcrumAuthDriverFactory(this)
        fulcrumDb = createDb(driver)
        fulcrumAuthQuery = fulcrumDb.fulcrumAuthModelQueries

        job = Job()
        api = FulcrumApi()
    }

    private fun getAccount(user: String, password: String) {
        val auth = Base64.getEncoder().encodeToString("$user:$password".toByteArray())
        api.getAccount(
            authorization = auth,
            success = ::parseUser,
            failure = ::handleError
        )
    }

    private fun parseUser(response: FulcrumAuthenticationResponse) {
        launch(Main) {
            Log.d("Context", "User ID: ${response.user.id}")

            val id = response.user.id
            val firstName = response.user.first_name
            val lastName = response.user.last_name
            val email = response.user.email

            fulcrumAuthQuery.insertUsers(id, firstName, lastName, email)

            for (context in response.user.contexts) {
                val name = context.name
                val contextId = context.id
                val apiToken = context.api_token

                fulcrumAuthQuery.insertOrgs(contextId, id, name, apiToken)

                Log.d("Context", "$name | Org ID : $contextId | Api Token : $apiToken")
            }
        }
    }

    private fun handleError(ex: Throwable) {
        launch(Main) {
            val msg = ex.message
            Log.d("Weather Response Error", msg)
        }
    }
}
