import {IAddress} from "./IAddress";

export interface IOrder {
  id: string,
  orderDate: string,
  orderStatus: string,
  buyerEmail: string,
  total: number,
  shippingAddress: IAddress,
  orderItems: IOrderItem[]
}

export interface IOrderItem {
  id: number,
  productItemId: string,
  productName: string,
  pictureUrl: string,
  unitPrice: number,
  quantity: number
}
