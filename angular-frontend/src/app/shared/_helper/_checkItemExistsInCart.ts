import {ICartItem} from "../_models/ICart";

export const _checkItemExistsInCart = (c: ICartItem[], productId: string) => {
  return c.some((e: ICartItem) => e.productId === productId);
};

export const _findItemExistsInCart = (c: ICartItem[], productId: string) => {
  if (c) {
    return c.find((e: ICartItem) => e.productId === productId)
  }
  return null;
};
