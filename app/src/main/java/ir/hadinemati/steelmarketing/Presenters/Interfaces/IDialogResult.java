package ir.hadinemati.steelmarketing.Presenters.Interfaces;

import ir.hadinemati.steelmarketing.Views.DialogQuestion;

public interface IDialogResult {
    void closed(DialogQuestion.DialogCloseStates state);
}
