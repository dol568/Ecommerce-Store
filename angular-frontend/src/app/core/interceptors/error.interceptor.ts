import {HttpInterceptorFn} from '@angular/common/http';
import {catchError, throwError} from "rxjs";
import {_client_notfound, _client_servererror} from "../../shared/_constVars/_client_consts";
import {inject} from "@angular/core";
import {Router} from "@angular/router";
import {SnackbarService} from "../_services/snackbar.service";

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const snackbar = inject(SnackbarService);
  return next(req).pipe(
    catchError((error) => {
      if (error) {
        if (error.status === 404) {
          router.navigate([`/${_client_notfound}`]);
        }
        if (error.status === 500) {
          router.navigate([`/${_client_servererror}`]);
        }
        snackbar.error(error.error.message)
      }
      return throwError(() => error);
    })
  );
};
