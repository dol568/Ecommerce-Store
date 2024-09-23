import {Routes} from "@angular/router";
import {RegisterComponent} from "./register/register.component";
import {_client_signin, _client_signup} from "../shared/_constVars/_client_consts";
import {LoginComponent} from "./login/login.component";

export const ACCOUNT_ROUTES: Routes = [
    { path: _client_signin, component: LoginComponent},
    { path: _client_signup, component: RegisterComponent}
]
