package Chen.Powers;

import Chen.Abstracts.Power;
import Chen.ChenMod;
import Chen.Util.PowerImages;
import Chen.Util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.*;

import static Chen.Util.PowerImages.PowerPath;

public class Disoriented extends Power {
    public static final String NAME = "Disoriented";
    public static final PowerType TYPE = PowerType.DEBUFF;
    public static final boolean TURN_BASED = true;

    public static final String POWER_ID = ChenMod.makeID(NAME);

    public Disoriented(final AbstractCreature owner, final int amount) {
        super(NAME, TYPE, TURN_BASED, owner, null, amount);
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (this.amount > 0 && damageAmount > 0)
        {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -damageAmount), -damageAmount));
            if (!this.owner.hasPower("Artifact")) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new GainStrengthPower(this.owner, damageAmount), damageAmount));
            }

            this.amount--;
            this.flash();
        }

        if (this.amount <= 0)
        {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }

        return damageAmount;
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
    }

    public void atEndOfRound() {
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    @Override
    public void updateDescription() {
        this.description = descriptions[0];
    }
}