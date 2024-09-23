import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AccountService} from "../_services/account.service";
import {catchError, map, of} from "rxjs";
import {_client_account, _client_signin} from "../../shared/_constVars/_client_consts";

export const authGuard: CanActivateFn = (route, state) => {
  const accountService = inject(AccountService);
  const router = inject(Router);

  return accountService.currentUser$.pipe(
      map((auth) => {
        if (auth || localStorage.getItem('token')) {
          return true;
        } else {
          router.navigate([_client_account + '/' + _client_signin]);
          return false;
        }
      }),
      catchError(() => {
        router.navigate([_client_account + '/' + _client_signin]);
        return of(false);
      })
  );
};
