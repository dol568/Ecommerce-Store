import {ApplicationConfig} from '@angular/core';
import {provideRouter} from '@angular/router';
import {routes} from './app.routes';
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {jwtInterceptor} from "./core/interceptors/jwt.interceptor";
import {errorInterceptor} from "./core/interceptors/error.interceptor";
import {loadingInterceptor} from "./core/interceptors/loading.interceptor";
import {provideAnimations} from "@angular/platform-browser/animations";

export const appConfig: ApplicationConfig = {
    providers: [
        provideRouter(routes),
        provideHttpClient(withInterceptors(
            [jwtInterceptor, errorInterceptor, loadingInterceptor]
        )),
        provideAnimations()
    ]
};