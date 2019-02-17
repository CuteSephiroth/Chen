package Chen.Actions.GenericActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.function.Predicate;

public class ModifyRandomCardCost extends AbstractGameAction {
    private CardGroup source;
    private Predicate<AbstractCard> condition;
    private boolean forCombat;

    public ModifyRandomCardCost(CardGroup source, int amount)
    {
        this(source, amount, false, (c)->true);
    }
    public ModifyRandomCardCost(CardGroup source, int amount, boolean forCombat, Predicate<AbstractCard> condition)
    {
        this.source = source;
        this.amount = amount;
        this.condition = condition;
        this.forCombat = forCombat;

        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.amount != 0)
        {
            CardGroup possibleCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for (AbstractCard c : source.group)
            {
                if (condition.test(c))
                {
                    possibleCards.addToRandomSpot(c);
                }
            }

            if (possibleCards.size() > 0)
            {
                AbstractCard toModify = possibleCards.getTopCard();

                if (forCombat)
                {
                    toModify.modifyCostForCombat(this.amount);
                }
                else
                {
                    toModify.modifyCostForTurn(this.amount);
                }
            }
        }



        this.isDone = true;
    }
}
