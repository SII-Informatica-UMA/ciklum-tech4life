import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { CentroList } from './app/CentroList';

bootstrapApplication(CentroList, appConfig)
  .catch((err) => console.error(err));
