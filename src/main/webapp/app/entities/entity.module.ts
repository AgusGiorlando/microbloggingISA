import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MicrobloggingIsaPublisherModule } from './publisher/publisher.module';
import { MicrobloggingIsaTagModule } from './tag/tag.module';
import { MicrobloggingIsaPublicationModule } from './publication/publication.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        MicrobloggingIsaPublisherModule,
        MicrobloggingIsaTagModule,
        MicrobloggingIsaPublicationModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MicrobloggingIsaEntityModule {}
