import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable, of} from "rxjs";
import {_api_brands, _api_default, _api_products, _api_types} from "../../shared/_constVars/_api_consts";
import {ShopParams} from "../../shared/_models/ShopParams";
import {_paramsAppend} from "../../shared/_helper/_paramsAppend";
import {IApiResponsePage} from "../../shared/_models/IApiResponsePage";
import {IApiResponse} from "../../shared/_models/IApiResponse";
import {IProduct} from "../../shared/_models/IProduct";
import {IBrand} from "../../shared/_models/IBrand";
import {IType} from "../../shared/_models/IType";
import {IPage} from "../../shared/_models/IPage";

@Injectable({
    providedIn: 'root'
})
export class ProductsService {
    #http = inject(HttpClient);
    products: IProduct[] | undefined = [];
    brands: IBrand[] = [];
    types: IType[] = [];
    shopParams = new ShopParams();
    searchUrl = _api_default + _api_products;
    currentPage: number = 1;

    constructor() {
    }

    products$(): Observable<IApiResponsePage<IPage<IProduct>>> {
        let params = _paramsAppend(this.shopParams);

        return this.#http
            .get<IApiResponsePage<IPage<IProduct>>>(this.searchUrl, {observe: 'response', params})
            .pipe(
                map((response) => {
                    const data = response.body;
                    this.products = data.data.page.content;
                    return data;
                })
            );
    }

    product$(id: string): Observable<IProduct> {
        const product = this.products.find((p) => p.id === id);
        if (product) return of(product);

        return this.#http.get<IProduct>(this.searchUrl + '/' + id);
    }

    brands$(): Observable<IBrand[]> {
        if (this.brands.length > 0) return of(this.brands);

        return this.#http.get<IApiResponse<IBrand[]>>(this.searchUrl + _api_brands).pipe(
            map((response) => {
                let brands = response.data;
                this.brands = brands;
                return brands;
            })
        );
    }

    types$(): Observable<IType[]> {
        if (this.types.length > 0) return of(this.types);

        return this.#http.get<IApiResponse<IType[]>>(this.searchUrl + _api_types).pipe(
            map((response) => {
                let types = response.data;
                this.types = types;
                return types;
            })
        );
    }

    getShopParams() {
        return this.shopParams;
    }

    setShopParams(params: ShopParams) {
        this.shopParams = params;
    }
}
