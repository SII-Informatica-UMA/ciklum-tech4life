import { bootstrapApplication } from '@angular/platform-browser';
import { CentroList } from './app/CentroList';
import { config } from './app/app.config.server';

const bootstrap = () => bootstrapApplication(CentroList, config);

export default bootstrap;
