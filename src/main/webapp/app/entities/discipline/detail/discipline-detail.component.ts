import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDiscipline } from '../discipline.model';

@Component({
  selector: 'jhi-discipline-detail',
  templateUrl: './discipline-detail.component.html',
})
export class DisciplineDetailComponent implements OnInit {
  discipline: IDiscipline | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ discipline }) => {
      this.discipline = discipline;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
