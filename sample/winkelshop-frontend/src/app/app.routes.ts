import { Routes } from '@angular/router';

import { authGuard, guestOnlyGuard } from './auth/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    canActivate: [guestOnlyGuard],
    loadComponent: () =>
      import('./auth/login-page').then(
        (module) => module.LoginPage
      )
  },
  {
    path: '',
    loadComponent: () =>
      import('./winkelshop/winkelshop-page').then(
        (module) => module.WinkelshopPage
      )
  },
  {
    path: 'checkout',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./checkout/checkout-page').then(
        (module) => module.CheckoutPage
      )
  },
  {
    path: '**',
    redirectTo: ''
  }
];
