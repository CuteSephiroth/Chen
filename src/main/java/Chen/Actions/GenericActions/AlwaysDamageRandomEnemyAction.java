package Chen.Actions.GenericActions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class AlwaysDamageRandomEnemyAction extends AbstractGameAction {
    private DamageInfo info;
    private static final float DURATION = 0.1F;
    private static final float POST_ATTACK_WAIT_DUR = 0.1F;

    public AlwaysDamageRandomEnemyAction(DamageInfo info, AttackEffect effect) {
        this.info = info;
        this.setValues(null, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = DURATION;
    }

    public void update() {
        this.target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
        if (this.shouldCancelAction()) {
            this.isDone = true;
        } else if (target != null && !target.isDeadOrEscaped()) {
            if (this.duration == DURATION) {
                this.target.damageFlash = true;
                this.target.damageFlashFrames = 4;
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            }

            this.tickDuration();
            if (this.isDone) {
                if (this.attackEffect == AttackEffect.POISON) {
                    this.target.tint.color = Color.CHARTREUSE.cpy();
                    this.target.tint.changeColor(Color.WHITE.cpy());
                } else if (this.attackEffect == AttackEffect.FIRE) {
                    this.target.tint.color = Color.RED.cpy();
                    this.target.tint.changeColor(Color.WHITE.cpy());
                }

                this.target.damage(this.info);
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                AbstractDungeon.actionManager.addToTop(new WaitAction(POST_ATTACK_WAIT_DUR));
            }

        }
        else
        {
            this.isDone = true;
        }
    }
}

