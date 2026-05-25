import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import type {
  CartAddItemRequest,
  CartResponse,
  CartUpdateItemRequest,
  CheckoutRequest,
  CheckoutResponse,
  LoginRequest,
  LoginResponse
} from './auth.models';

@Injectable({ providedIn: 'root' })
export class AuthApiService {
  private readonly httpClient = inject(HttpClient);
  private readonly apiBaseUrl = '/api';

  public login(payload: LoginRequest) {
    return this.httpClient.post<LoginResponse>(`${this.apiBaseUrl}/auth/login`, payload);
  }

  public logout() {
    return this.httpClient.post<void>(`${this.apiBaseUrl}/auth/logout`, {});
  }

  public addCartItem(payload: CartAddItemRequest) {
    return this.httpClient.post<CartResponse>(`${this.apiBaseUrl}/cart/items`, payload);
  }

  public getCart() {
    return this.httpClient.get<CartResponse>(`${this.apiBaseUrl}/cart`);
  }

  public updateCartItem(itemId: string, payload: CartUpdateItemRequest) {
    return this.httpClient.patch<CartResponse>(`${this.apiBaseUrl}/cart/items/${itemId}`, payload);
  }

  public removeCartItem(itemId: string) {
    return this.httpClient.delete<CartResponse>(`${this.apiBaseUrl}/cart/items/${itemId}`);
  }

  public checkout(payload: CheckoutRequest) {
    return this.httpClient.post<CheckoutResponse>(`${this.apiBaseUrl}/checkout`, payload);
  }
}
