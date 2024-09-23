import {Routes} from "@angular/router";
import {ProductDetailComponent} from "./product-detail/product-detail.component";
import {ShopComponent} from "./shop.component";

export const SHOP_ROUTES: Routes = [
    { path: '', component: ShopComponent },
    { path: ':id', component: ProductDetailComponent }
]
