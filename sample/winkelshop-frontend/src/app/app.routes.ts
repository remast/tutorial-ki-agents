import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./winkelshop/winkelshop-page').then(
        (module) => module.WinkelshopPage
      )
  },
  {
    path: '**',
    redirectTo: ''
  }
];
