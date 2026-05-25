export interface AuthUser {
  id: string;
  name: string;
  email: string;
  role: string;
  createdAt: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  data: {
    accessToken: string;
    tokenType: string;
    expiresIn: number;
    user: AuthUser;
  };
}

export interface CartAddItemRequest {
  productId: string;
  quantity: number;
}

export interface CartItem {
  itemId: string;
  productId: string;
  name: string;
  quantity: number;
  unitPrice: number;
}

export interface CartData {
  items: CartItem[];
  currency: string;
}

export interface CartResponse {
  data: CartData;
}

export interface CartUpdateItemRequest {
  quantity: number;
}

export interface CheckoutShippingAddress {
  fullName: string;
  street: string;
  city: string;
  postalCode: string;
  country: string;
}

export interface CheckoutRequest {
  paymentMethod: string;
  shippingAddress: CheckoutShippingAddress;
}

export interface CheckoutOrder {
  id: string;
  userId: string;
  status: string;
  paymentMethod: string;
  total: number;
  currency: string;
  shippingFullName: string;
  shippingStreet: string;
  shippingCity: string;
  shippingPostalCode: string;
  shippingCountry: string;
  createdAt: string;
}

export interface CheckoutOrderItem {
  id: string;
  orderId: string;
  productId: string;
  name: string;
  quantity: number;
  unitPrice: number;
}

export interface CheckoutResponse {
  data: {
    order: CheckoutOrder;
    items: CheckoutOrderItem[];
  };
}
