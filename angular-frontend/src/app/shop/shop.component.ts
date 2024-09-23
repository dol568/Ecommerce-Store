import {Component, ElementRef, inject, OnInit, ViewChild} from '@angular/core';
import {ShopParams} from "../shared/_models/ShopParams";
import {ProductsService} from "../core/_services/products.service";
import {IProduct} from "../shared/_models/IProduct";
import {IBrand} from "../shared/_models/IBrand";
import {IType} from "../shared/_models/IType";
import {PagerComponent} from "../shared/components/pager/pager.component";
import {ProductItemComponent} from "./product-item/product-item.component";
import {PagingHeaderComponent} from "../shared/components/paging-header/paging-header.component";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-shop',
  standalone: true,
  imports: [PagerComponent, ProductItemComponent, PagingHeaderComponent, CommonModule],
  templateUrl: './shop.component.html',
  styleUrl: './shop.component.scss'
})
export class ShopComponent implements OnInit {
  #productService = inject(ProductsService);
  @ViewChild('search') searchTerm: ElementRef;
  products: IProduct[];
  brands: IBrand[];
  types: IType[];

  totalElements: number;
  totalPages: number;

  shopParams: ShopParams;

  sortOptions = [
    {name: 'Alphabetical', value: 'name,asc'},
    {name: 'Price: Low to High', value: 'unitPrice,asc'},
    {name: 'Price: High to Low', value: 'unitPrice,desc'},
  ];

  constructor() {
    this.shopParams = this.#productService.getShopParams();
    this.getProducts();
    this.getBrands();
    this.getTypes();
  }

  ngOnInit(): void {}

  getProducts() {
    this.#productService.products$().subscribe({
      next: response => {
        const data = response.data.page;
        const {totalElements, totalPages} = data;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.products = data.content;
      },
      error: err => console.error(err)
    });
  }

  getBrands() {
    this.#productService.brands$().subscribe({
      next: response => this.brands = [{id: null, name: 'All'}, ...response],
      error: err => console.log(err)
    });
  }

  getTypes() {
    this.#productService.types$().subscribe({
      next: response => this.types = [{id: null, name: 'All'}, ...response],
      error: err => console.log(err)
    });
  }

  private filterData(params: ShopParams) {
    this.#productService.setShopParams(params);
    this.getProducts();
  }

  onBrandSelected(brandId: string) {
    const params = this.#productService.getShopParams();
    params.brand = brandId;
    params.page = 0;
    this.filterData(params);
  }

  onTypeSelected(typeId: string) {
    const params = this.#productService.getShopParams();
    params.type = typeId;
    params.page = 0;
    this.filterData(params);
  }

  onSortSelected(sort: string) {
    const params = this.#productService.getShopParams();
    params.sort = sort;
    this.filterData(params);
  }

  onPageChanged(page: number) {
    const params = this.#productService.getShopParams();
    if (params.page !== page) {
      params.page = page;
      this.filterData(params);
    }
  }

  onSearch() {
    const params = this.#productService.getShopParams();
    params.name = this.searchTerm.nativeElement.value;
    params.page = 0;
    this.filterData(params);
  }

  onReset() {
    if (this.searchTerm) {
      this.searchTerm.nativeElement.value = '';
    }
    this.shopParams = new ShopParams();
    this.#productService.setShopParams(this.shopParams);
    this.getProducts();
  }
}
