import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDiscipline } from '../discipline.model';
import { DisciplineService } from '../service/discipline.service';
import { DisciplineDeleteDialogComponent } from '../delete/discipline-delete-dialog.component';

@Component({
  selector: 'jhi-discipline',
  templateUrl: './discipline.component.html',
})
export class DisciplineComponent implements OnInit {
  disciplines?: IDiscipline[];
  isLoading = false;

  constructor(protected disciplineService: DisciplineService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.disciplineService.query().subscribe({
      next: (res: HttpResponse<IDiscipline[]>) => {
        this.isLoading = false;
        this.disciplines = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDiscipline): number {
    return item.id!;
  }

  delete(discipline: IDiscipline): void {
    const modalRef = this.modalService.open(DisciplineDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.discipline = discipline;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
