import {IBrand} from "./IBrand";
import {IType} from "./IType";

export interface IProduct{
  id: string;
  name: string;
  description: string;
  unitPrice: number;
  pictureUrl: string;
  brand: IBrand;
  type: IType;
}
