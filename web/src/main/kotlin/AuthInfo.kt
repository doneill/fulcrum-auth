import react.*

import styled.styledDiv

external interface AuthInfoProps: PropsWithChildren {
    var response: String
}

class AuthInfo: RComponent<AuthInfoProps, State>() {
    override fun RBuilder.render() {
        styledDiv {
            +props.response
        }
    }
}

fun RBuilder.authInfo(handler: AuthInfoProps.() -> Unit) {
    return child(AuthInfo::class) {
        this.attrs(handler)
    }
}