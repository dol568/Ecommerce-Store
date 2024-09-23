import {Component, inject, OnInit} from '@angular/core';
import {
  _client_account,
  _client_checkout, _client_order,
  _client_signin,
  _client_signup
} from "../../shared/_constVars/_client_consts";
import {Observable} from "rxjs";
import {IUser} from "../../shared/_models/IUser";
import {ICart} from "../../shared/_models/ICart";
import {AccountService} from "../_services/account.service";
import {CartService} from "../_services/cart.service";
import {RouterModule} from "@angular/router";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent implements OnInit {
  #accountService = inject(AccountService);
  #cartService = inject(CartService);
  login: string = _client_account + '/' + _client_signin;
  register: string = _client_account + '/' + _client_signup;
  checkout: string = `/${_client_checkout}/`;
  order: string = `/${_client_order}/`;
  currentUser$: Observable<IUser>;
  cart$: Observable<ICart>;

  constructor() {
  }

  ngOnInit(): void {
    this.currentUser$ = this.#accountService.currentUser$;
    this.cart$ = this.#cartService.cart$;
  }

  logout() {
    this.#accountService.logout();
    this.#cartService.logoutCart();
  }
}
