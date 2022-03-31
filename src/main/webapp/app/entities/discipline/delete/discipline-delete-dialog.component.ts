import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDiscipline } from '../discipline.model';
import { DisciplineService } from '../service/discipline.service';

@Component({
  templateUrl: './discipline-delete-dialog.component.html',
})
export class DisciplineDeleteDialogComponent {
  discipline?: IDiscipline;

  constructor(protected disciplineService: DisciplineService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.disciplineService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
