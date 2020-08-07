import react.*
import styled.styledDiv

external interface AuthInfoProps: RProps {
    var response: String
}

class AuthInfo: RComponent<AuthInfoProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            +props.response
        }
    }
}

fun RBuilder.authInfo(handler: AuthInfoProps.() -> Unit): ReactElement {
    return child(AuthInfo::class) {
        this.attrs(handler)
    }
}