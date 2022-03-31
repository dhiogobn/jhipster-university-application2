import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DisciplineComponent } from './list/discipline.component';
import { DisciplineDetailComponent } from './detail/discipline-detail.component';
import { DisciplineUpdateComponent } from './update/discipline-update.component';
import { DisciplineDeleteDialogComponent } from './delete/discipline-delete-dialog.component';
import { DisciplineRoutingModule } from './route/discipline-routing.module';

@NgModule({
  imports: [SharedModule, DisciplineRoutingModule],
  declarations: [DisciplineComponent, DisciplineDetailComponent, DisciplineUpdateComponent, DisciplineDeleteDialogComponent],
  entryComponents: [DisciplineDeleteDialogComponent],
})
export class DisciplineModule {}
