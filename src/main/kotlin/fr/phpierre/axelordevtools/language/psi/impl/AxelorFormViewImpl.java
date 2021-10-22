package fr.phpierre.axelordevtools.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import fr.phpierre.axelordevtools.language.psi.AxelorFormView;
import org.jetbrains.annotations.NotNull;

public abstract class AxelorFormViewImpl extends ASTWrapperPsiElement implements AxelorFormView {

    public AxelorFormViewImpl(@NotNull ASTNode node) {
        super(node);
    }

}