import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { shareReplay } from 'rxjs';

export interface Category {
  id: string;
  name: string;
  description: string;
}

export interface CategoriesResponse {
  data: Category[];
  meta: {
    count: number;
  };
}

export interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  currency: string;
  inStock: boolean;
  stock: number;
  categoryId: string;
  imageUrl: string;
}

export interface ProductPageResponse {
  content: Product[];
}

@Injectable({ providedIn: 'root' })
export class WinkelshopApiService {
  private readonly httpClient = inject(HttpClient);
  private readonly apiBaseUrl = '/api';

  public getCategoriesResponse() {
    return this.httpClient
      .get<CategoriesResponse>(`${this.apiBaseUrl}/categories`)
      .pipe(shareReplay({ bufferSize: 1, refCount: true }));
  }

  public getProductsResponse(categoryId: string | null) {
    const params = categoryId
      ? new HttpParams().set('category', categoryId)
      : undefined;

    return this.httpClient
      .get<ProductPageResponse>(`${this.apiBaseUrl}/products`, { params })
      .pipe(shareReplay({ bufferSize: 1, refCount: true }));
  }
}
