export interface ICart {
  total: number;
  totalItems: number;
  cartItems: ICartItem[];
}

export interface ICartItem {
  id: number;
  productId: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  pictureUrl: string;
}

export interface IAddCart {
  productId: string;
  quantity: number
}
