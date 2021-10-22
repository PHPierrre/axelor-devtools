package fr.phpierre.axelordevtools.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import fr.phpierre.axelordevtools.language.psi.AxelorGridView;
import org.jetbrains.annotations.NotNull;

public abstract class AxelorGridViewImpl extends ASTWrapperPsiElement implements AxelorGridView {

    public AxelorGridViewImpl(@NotNull ASTNode node) {
        super(node);
    }

}
