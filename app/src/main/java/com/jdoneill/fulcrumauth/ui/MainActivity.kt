package com.jdoneill.fulcrumauth.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.jdoneill.fulcrumauth.api.FulcrumApi

import com.jdoneill.fulcrumauth.common.FulcrumAuthDriverFactory
import com.jdoneill.fulcrumauth.common.createDb
import com.jdoneill.fulcrumauth.R
import com.jdoneill.fulcrumauth.db.FulcrumAuth
import com.jdoneill.fulcrumauth.db.FulcrumAuthModelQueries
import com.jdoneill.fulcrumauth.db.SelectJoinUserOrgByUserId
import com.jdoneill.fulcrumauth.model.Contexts
import com.jdoneill.fulcrumauth.model.FulcrumAuthenticationResponse
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

import java.util.Base64

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    private lateinit var api: FulcrumApi

    private lateinit var fulcrumDb: FulcrumAuth
    private lateinit var fulcrumAuthQuery: FulcrumAuthModelQueries

    override val coroutineContext: CoroutineContext
        get() = job + Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Organization")

        val names = arrayOfNulls<String>(accounts.size)
        for ( i in accounts.indices) {
            names[i] = accounts[i].name
        }

        builder.setItems(names) { _, which -> setAccount(accounts[which]) }

        val alert = builder.create()
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
            Log.d("Authorization Response Error", msg)
        }
    }
}
