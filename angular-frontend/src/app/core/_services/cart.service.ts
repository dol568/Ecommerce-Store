import {inject, Injectable} from '@angular/core';
import {_api_cart, _api_default} from "../../shared/_constVars/_api_consts";
import {BehaviorSubject, map, Observable, of} from "rxjs";
import {IAddCart, ICart} from "../../shared/_models/ICart";
import {HttpClient} from "@angular/common/http";
import {IApiResponse} from "../../shared/_models/IApiResponse";

@Injectable({
    providedIn: 'root'
})
export class CartService {
    #http = inject(HttpClient);
    baseUrl = _api_default + _api_cart;
    private cartSource = new BehaviorSubject<ICart>(null);
    cart$ = this.cartSource.asObservable();

    constructor() {
    }

    getCart(): Observable<ICart> {
        if (localStorage.getItem('token')) {
            return this.#http.get<IApiResponse<ICart>>(this.baseUrl).pipe(
                map((response) => {
                    let cart = response.data;
                    this.cartSource.next(cart);
                    return cart;
                })
            )
        } else {
            this.cartSource.next(null);
            return of(null);
        }
    }

    editCart(data: IAddCart) {
        return this.#http.post(this.baseUrl, data).pipe(
            map((response: IApiResponse<ICart>) => {
                let cart = response.data;
                this.cartSource.next(cart);
                return cart.cartItems;
            })
        );
    }

    getCurrentCartValue() {
        return this.cartSource.value;
    }

    removeItemFromCart(itemId: string) {
        return this.#http.delete(this.baseUrl + '/' + itemId).pipe(
            map((response: IApiResponse<ICart>) => {
                let cart = response.data;
                this.cartSource.next(cart);
                return cart.cartItems;
            })
        );
    }

    clearCart() {
        return this.#http.delete(this.baseUrl).pipe(
            map((response: IApiResponse<ICart>) => {
                let cart = response.data;
                this.cartSource.next(cart);
                return cart.cartItems;
            })
        );
    }

    logoutCart() {
        this.cartSource.next(null);
    }
}
