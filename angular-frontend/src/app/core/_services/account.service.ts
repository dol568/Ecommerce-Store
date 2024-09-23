import {inject, Injectable} from '@angular/core';
import {_api_account, _api_address, _api_default, _api_login, _api_signup} from "../../shared/_constVars/_api_consts";
import {_client_home} from "../../shared/_constVars/_client_consts";
import {BehaviorSubject, map, Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";
import {IApiResponse} from "../../shared/_models/IApiResponse";
import {ILogin, IRegister} from "../../shared/_models/IAuthForm";
import {IUser} from "../../shared/_models/IUser";
import {IAddress} from "../../shared/_models/IAddress";

@Injectable({
    providedIn: 'root'
})
export class AccountService {
    #http = inject(HttpClient);
    #router = inject(Router);
    baseUrl = _api_default + _api_account;
    private currentUserSource = new BehaviorSubject<IUser>(null)
    currentUser$ = this.currentUserSource.asObservable();

    constructor() {}

    loadCurrentUser(token: string) {
        if (token === null) {
            this.currentUserSource.next(null);
            return null;
        }
        let headers = new HttpHeaders();
        headers = headers.set('Authorization', `Bearer ${token}`);
        return this.#http.get(this.baseUrl, {headers}).pipe(
            map((response: IApiResponse<IUser>) => {
                    let user = response.data;
                    if (user) {
                        localStorage.setItem("token", user.token);
                        this.currentUserSource.next(user);
                    }
                }
            ));
    }

    login(values: ILogin) {
        return this.#http.post(_api_default + _api_login, values).pipe(
            map((response: IApiResponse<IUser>) => {
                    let user = response.data;
                    if (user) {
                        localStorage.setItem("token", user.token);
                        this.currentUserSource.next(user);
                    }
                }
            ));
    }

    register(values: IRegister) {
        return this.#http.post(_api_default + _api_signup, values).pipe(
            map((response: IApiResponse<IUser>) => {
                let user = response.data;
                if (user) {
                    localStorage.setItem("token", user.token);
                    this.currentUserSource.next(user);
                }
            })
        );
    }

    logout() {
        localStorage.removeItem('token');
        this.currentUserSource.next(null);
        this.#router.navigate([_client_home]);
    }

    address$(): Observable<IAddress> {
        return this.#http.get<IApiResponse<IAddress>>(this.baseUrl + _api_address).pipe(
            map((response) => {
                return response.data;
            })
        );
    }
}
