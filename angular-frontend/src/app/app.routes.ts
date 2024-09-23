import {Routes} from '@angular/router';
import {
    _client_account,
    _client_checkout, _client_notfound,
    _client_order,
    _client_servererror,
    _client_shop, _client_thankyou
} from "./shared/_constVars/_client_consts";
import {authGuard} from "./core/_guard/auth.guard";

export const routes: Routes = [
    {path: '', redirectTo: _client_shop, pathMatch: 'full'},
    {
        path: _client_shop,
        loadChildren: () => import('./shop/shop.routes')
            .then(r => r.SHOP_ROUTES)
    },
    {
        path: _client_account,
        loadChildren: () => import('./account/account.routes')
            .then(r => r.ACCOUNT_ROUTES)
    },
    {
        path: _client_checkout,
        canActivate: [authGuard],
        loadComponent: () => import('./checkout/checkout.component')
            .then(m => m.CheckoutComponent)
    },
    {
        path: _client_order,
        canActivate: [authGuard],
        loadComponent: () => import('./order/order.component')
            .then(m => m.OrderComponent)
    },
    {
        path: _client_thankyou,
        loadComponent: () => import('./core/thankyou/thankyou.component')
            .then(m => m.ThankyouComponent)
    },
    {
        path: _client_servererror,
        loadComponent: () => import('./core/server-error/server-error.component')
            .then(m => m.ServerErrorComponent)
    },
    {
        path: _client_notfound,
        loadComponent: () => import('./core/notfound/notfound.component')
            .then(m => m.NotfoundComponent)
    },
    {path: '**', redirectTo: 'not-found', pathMatch: 'full'}
];
