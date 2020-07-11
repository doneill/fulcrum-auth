package com.jdoneill.ktmultiplatform.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.jdoneill.api.FulcrumApi

import com.jdoneill.common.FulcrumAuthDriverFactory
import com.jdoneill.common.createDb
import com.jdoneill.db.FulcrumAuth
import com.jdoneill.db.FulcrumAuthModelQueries
import com.jdoneill.db.SelectJoinUserOrgByUserId
import com.jdoneill.ktmultiplatform.R
import com.jdoneill.model.Contexts
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
    private lateinit var loginInfoView: TextView

    private lateinit var fulcrumDb: FulcrumAuth
    private lateinit var fulcrumAuthQuery: FulcrumAuthModelQueries

    override val coroutineContext: CoroutineContext
        get() = job + Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userEmailField = findViewById(R.id.user_email_view)
        userPasswordField = findViewById(R.id.user_password_view)
        loginInfoView = findViewById(R.id.login_info_view)

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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun showAccounts(accounts: List<Contexts>) {
        val b = AlertDialog.Builder(this)
        b.setTitle("Select Organization")

        val names = arrayOfNulls<String>(accounts.size)
        for ( i in accounts.indices) {
            names[i] = accounts[i].name
        }

        b.setItems(names) { _, which -> setAccount(accounts[which]) }

        val alert = b.create()
        alert.show()
    }

    private fun getAccount(user: String, password: String) {
        val auth = Base64.getEncoder().encodeToString("$user:$password".toByteArray())
        api.getAccount(
            authorization = auth,
            success = ::parseAccount,
            failure = ::handleError
        )
    }

    private fun setAccount(context: Contexts) {
        val orgId = context.id
        val organizations = fulcrumAuthQuery.selectUserByOrganizationId(orgId).executeAsList()

        for (org in organizations) {
            val infos = fulcrumAuthQuery.selectJoinUserOrgByUserId(org.user_id).executeAsList()
            showLoginInfo(infos)
        }

    }

    private fun parseAccount(response: FulcrumAuthenticationResponse) {
        launch(Main) {
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
            }

            if ( response.user.contexts.size > 1 ) {
                showAccounts(response.user.contexts)
            } else {
                val infos = fulcrumAuthQuery.selectJoinUserOrgByUserId(id).executeAsList()

                for ( info in infos ) {
                    Log.d("FulcrumAuth", info.toString())
                }

                showLoginInfo(infos)
            }
        }
    }

    private fun showLoginInfo(infos: List<SelectJoinUserOrgByUserId>) {
        userEmailField.visibility = View.INVISIBLE
        userPasswordField.visibility = View.INVISIBLE
        loginButton.visibility = View.INVISIBLE

        loginInfoView.visibility = View.VISIBLE

        var firstName = ""
        var lastName = ""
        var org = ""
        var token = ""

        for ( i in infos.indices ) {
            firstName = infos[i].first_name
            lastName = infos[i].last_name
            org = infos[i].name
            token = infos[i].token
        }

        loginInfoView.text = getString(R.string.user_info, firstName, lastName, org, token)
    }

    private fun handleError(ex: Throwable) {
        launch(Main) {
            val msg = ex.message
            Log.d("Weather Response Error", msg)
        }
    }
}
