package fr.phpierre.axelordevtools.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import fr.phpierre.axelordevtools.language.psi.AxelorView;
import org.jetbrains.annotations.NotNull;

public abstract class AxelorViewImpl extends ASTWrapperPsiElement implements AxelorView {

    public AxelorViewImpl(@NotNull ASTNode node) {
        super(node);
    }

}