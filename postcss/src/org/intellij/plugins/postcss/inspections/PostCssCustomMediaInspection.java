package org.intellij.plugins.postcss.inspections;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElementVisitor;
import org.intellij.plugins.postcss.PostCssBundle;
import org.intellij.plugins.postcss.actions.PostCssAddPrefixQuickFix;
import org.intellij.plugins.postcss.psi.PostCssCustomMedia;
import org.intellij.plugins.postcss.psi.PostCssElementGenerator;
import org.intellij.plugins.postcss.psi.impl.PostCssCustomMediaAtRuleImpl;
import org.intellij.plugins.postcss.psi.impl.PostCssElementVisitor;
import org.jetbrains.annotations.NotNull;

public class PostCssCustomMediaInspection extends PostCssBaseInspection {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
    return new PostCssElementVisitor() {
      @Override
      public void visitPostCssCustomMediaAtRule(PostCssCustomMediaAtRuleImpl postCssCustomMediaAtRule) {
        PostCssCustomMedia customMedia = postCssCustomMediaAtRule.getCustomMedia();
        if (customMedia == null) return;
        String text = customMedia.getText();
        if (text.equals("--")) {
          holder.registerProblem(customMedia, PostCssBundle.message("annotator.custom.media.name.should.not.be.empty"));
        }
        else if (!StringUtil.startsWith(text, "--")) {
          PostCssAddPrefixQuickFix quickFix = new PostCssAddPrefixQuickFix("annotator.add.prefix.to.custom.media.quickfix.name", "--",
                                                                           psi -> psi instanceof PostCssCustomMedia,
                                                                           p -> PostCssElementGenerator
                                                                             .createCustomMedia(p.first, p.second));
          holder.registerProblem(customMedia, PostCssBundle.message("annotator.custom.media.name.should.start.with"), quickFix);
        }
      }
    };
  }
}