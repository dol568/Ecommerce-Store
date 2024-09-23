import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {_api_default, _api_order} from "../../shared/_constVars/_api_consts";
import {IOrder} from "../../shared/_models/IOrder";
import {IAddress} from "../../shared/_models/IAddress";
import {IApiResponse} from "../../shared/_models/IApiResponse";

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  #http = inject(HttpClient);
  baseUrl = _api_default + _api_order;

  constructor() {
  }

  addOrder(address: IAddress): Observable<any> {
    return this.#http.post(this.baseUrl, address);
  }

  getOrders(): Observable<IOrder[]> {
    return this.#http.get<IApiResponse<IOrder[]>>(this.baseUrl).pipe(
        map(response => {
          return response.data;
        })
      );
  }

  getOrderById(id: number) {
    this.#http.get(this.baseUrl + '/' + id);
  }
}
