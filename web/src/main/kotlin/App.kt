import com.jdoneill.fulcrumauth.api.FulcrumApi
import com.jdoneill.fulcrumauth.model.FulcrumAuthenticationResponse

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction

import org.w3c.dom.HTMLInputElement

import react.*
import react.dom.*
import styled.*

external interface AppState : State {
    var htmlResponse: String
    var email: String
    var password: String
}

class App : RComponent<RProps, AppState>() {

    private lateinit var api: FulcrumApi

    override fun AppState.init() {
        api = FulcrumApi()
    }

    private fun parseAccount(response: FulcrumAuthenticationResponse) {
        val firstName = response.user.first_name
        val lastName = response.user.last_name

        var apiToken = ""

        for (context in response.user.contexts) {
            apiToken = context.api_token
        }

        setState {
            htmlResponse = """
                Success!
                You are logged in as $firstName $lastName
                Your API token is $apiToken
            """.trimIndent()
        }
    }

    private fun handleError(ex: Throwable) {
        val msg = ex.message

        setState {
            htmlResponse = """
                Error: $msg
            """.trimIndent()
        }

        console.log("Authorization Response", msg)
    }

    private fun jsEncode(auth: Any): String {
        return js("btoa(auth)") as String
    }

    override fun RBuilder.render() {
        styledBody {
            css {
                backgroundColor = Color.whiteSmoke
            }
        }

        styledH1 {
            css {
                fontFamily = "Verdana, Geneva, sans-serif"
            }
            +"Fulcrum Auth"
        }

        styledInput( type = InputType.text, name = "email" ) {
            key = "email"

            attrs {
                placeholder = "Email"
                onChangeFunction = {
                    val target = it.target as HTMLInputElement
                    setState {
                        email = target.value
                    }
                }
            }
        }

        styledInput( type = InputType.password, name = "password" ) {
            key = "password"

            attrs {
                placeholder = "Password"
                onChangeFunction = {
                    val target = it.target as HTMLInputElement
                    setState {
                        password = target.value
                    }
                }
            }
        }

        styledButton {
            css {
                borderStyle = BorderStyle.solid
                width = 145.px
                backgroundColor = Color.yellow
                fontFamily = "Tahoma, Geneva, sans-serif"
            }
            attrs {
                onClickFunction = {

                    val currentEmail = state.email
                    val currentPassword = state.password

                    val auth = jsEncode("$currentEmail:$currentPassword")

                    val mainScope = MainScope()
                    mainScope.launch {
                        try {
                            val response = api.getAccount(auth)
                            parseAccount(response)
                        } catch (t: Throwable) {
                            handleError(t)
                        }
                    }
                }
            }
            +"Login"
        }

        styledDiv {
            css {
                padding(vertical = 16.px)
                fontFamily = "Tahoma, Geneva, sans-serif"
            }
            authInfo {
                response = state.htmlResponse
            }
        }
    }
}