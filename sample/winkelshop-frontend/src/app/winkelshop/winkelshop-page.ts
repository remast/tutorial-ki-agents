import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, distinctUntilChanged, map, of, switchMap, tap } from 'rxjs';

import {
  WinkelshopApiService,
  type Category,
  type CategoriesResponse,
  type Product,
  type ProductPageResponse
} from './winkelshop-api.service';

const EMPTY_CATEGORIES_RESPONSE: CategoriesResponse = {
  data: [],
  meta: { count: 0 }
};

@Component({
  selector: 'app-winkelshop-page',
  templateUrl: './winkelshop-page.html',
  styleUrl: './winkelshop-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class WinkelshopPage {
  private readonly winkelshopApiService = inject(WinkelshopApiService);

  protected readonly selectedCategoryId = signal<string | null>(null);
  protected readonly categoriesErrorMessage = signal<string | null>(null);
  protected readonly productsErrorMessage = signal<string | null>(null);

  protected readonly categoriesResource = toSignal(
    this.winkelshopApiService.getCategoriesResponse().pipe(
      tap(() => this.categoriesErrorMessage.set(null)),
      catchError(() => {
        this.categoriesErrorMessage.set('Kategorien konnten nicht geladen werden.');
        return of(EMPTY_CATEGORIES_RESPONSE);
      })
    ),
    { initialValue: EMPTY_CATEGORIES_RESPONSE }
  );

  private readonly productsResponse = toSignal<ProductPageResponse | null>(
    toObservable(this.selectedCategoryId).pipe(
      distinctUntilChanged(),
      switchMap((categoryId) => this.winkelshopApiService.getProductsResponse(categoryId)),
      map((response) => this.sortProducts(response)),
      tap(() => this.productsErrorMessage.set(null)),
      catchError(() => {
        this.productsErrorMessage.set('Produkte konnten nicht geladen werden.');
        return of(null);
      })
    ),
    { initialValue: null }
  );

  protected readonly categories = computed(() => this.categoriesResource().data);

  protected readonly products = computed(() => this.productsResponse()?.content ?? []);

  protected readonly hasProducts = computed(() => this.products().length > 0);

  protected readonly combinedErrorMessage = computed(() => {
    return this.categoriesErrorMessage() ?? this.productsErrorMessage();
  });

  private readonly numberFormatter = new Intl.NumberFormat('de-DE', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });

  protected readonly selectedCategoryName = computed(() => {
    const selectedCategoryId = this.selectedCategoryId();
    if (!selectedCategoryId) {
      return 'Alle Kategorien';
    }

    const category = this.categories().find((entry) => entry.id === selectedCategoryId);
    return category?.name ?? 'Kategorie';
  });

  protected showAllCategories(): void {
    this.selectedCategoryId.set(null);
  }

  protected selectCategory(categoryId: string): void {
    this.selectedCategoryId.set(categoryId);
  }

  protected trackByProductId(_index: number, product: Product): string {
    return product.id;
  }

  protected trackByCategoryId(_index: number, category: Category): string {
    return category.id;
  }

  protected formatPrice(price: number, currency: string): string {
    return `${this.numberFormatter.format(price)} ${currency}`;
  }

  protected productInitial(name: string): string {
    return name.slice(0, 1).toUpperCase();
  }

  private sortProducts(response: ProductPageResponse): ProductPageResponse {
    return {
      ...response,
      content: [...response.content].sort((leftProduct, rightProduct) => {
        return leftProduct.name.localeCompare(rightProduct.name, 'de');
      })
    };
  }
}
