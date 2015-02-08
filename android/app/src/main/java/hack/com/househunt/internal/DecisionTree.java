package hack.com.househunt.internal;

import java.util.Objects;

/**
 * Tree used to figure out which questions to ask the user during the initial questionaire.
 */
public class DecisionTree {

    private DecisionNode cur;

    public static final String INCOME_SCALE = "INCOME_SCALE";
    public static final String NUMBER_PICKER = "SCALE"; // 0 - 10 scale
    public static final String FIRST_TIME_HOME = "FIRST_TIME_HOME";
    public static final String YES_NO = "YES_NO";
    public static final String NUMBER = "NUMBER";
    public static final String RENT_OR_BUY = "RENT_OR_BUY";
    public static final String TEXT = "TEXT_ENTRY";

    public DecisionTree() {
        cur = new DecisionNode();
        setupDecisionTree();
    }

    private void setupDecisionTree() {
        DecisionNode location = this.cur;
        // Location
        location.question = "Where would you like to buy or rent a property?";
        location.option = TEXT;
        location.id = "locale";

        // Age
        DecisionNode age = new DecisionNode();
        age.question = "Which age group best describes you?";
        age.id = "age_range";

        // < 25 age node
        DecisionNode lessTwentyFive = new DecisionNode();
        lessTwentyFive.option = "Under 25";

        // 25 - 55 age node
        DecisionNode twentyFiftyFive = new DecisionNode();
        twentyFiftyFive.option = "25 - 55";

        // 55+ age node
        DecisionNode overFiftyFive = new DecisionNode();
        overFiftyFive.option = "55+";

        // Accessability node
        DecisionNode access = new DecisionNode();
        access.option = NUMBER_PICKER;
        access.question = "On a scale from 0 - 10 where 10 is the most important, " +
                "do you require additional amenities?";
        access.id = "amenities_weight";

        DecisionNode firstTimeHome = new DecisionNode();
        firstTimeHome.question = "Are you a first time home buyer?";
        firstTimeHome.option = FIRST_TIME_HOME;
        firstTimeHome.id = "first_home";

        DecisionNode categories = new DecisionNode();
        categories.question = "What category would you classify yourself as?";
        categories.id = "category";

        DecisionNode singleProfessional = new DecisionNode();
        singleProfessional.option = Constant.SINGLE_PROFESSIONAL_STR;

        DecisionNode workingIndividual = new DecisionNode();
        workingIndividual.option = Constant.WORKING_INDIVIDUAL_STR;

        DecisionNode singleParent = new DecisionNode();
        singleParent.option = Constant.SINGLE_PARENT_FAMILY_STR;

        DecisionNode modIncomeFamily = new DecisionNode();
        modIncomeFamily.option = Constant.MODERATE_INCOME_FAMILY_STR;

        DecisionNode duelProfFamily = new DecisionNode();
        duelProfFamily.option = Constant.DUAL_PROFESSIONAL_FAMILY_STR;

        // education node
        DecisionNode education = new DecisionNode();
        education.question = "On a scale from 1 - 10 where 10 is the most important, " +
                "how much do you value access to a good " +
                "school?";
        education.option = NUMBER_PICKER;
        education.id = "education_weight";

        // income node
        DecisionNode income = new DecisionNode();
        income.question = "Approximately how much do you earn?";
        income.option = INCOME_SCALE;
        income.id = "income";

        // voucher
        DecisionNode vouchers = new DecisionNode();
        vouchers.question = "Do you qualify for a voucher?";
        vouchers.option = YES_NO;
        vouchers.id = "voucher";

        // subsidies
        DecisionNode subsidies = new DecisionNode();
        subsidies.question = "Do you qualify for subsidies?";
        subsidies.option = YES_NO;
        subsidies.id = "subsidies";

        DecisionNode transportationCosts = new DecisionNode();
        transportationCosts.question = "On a scale from 1 - 10 where 10 is the most important, " +
                "how much do you value the " +
                "accessibility (transportation) of the area?";
        transportationCosts.option = NUMBER_PICKER;
        transportationCosts.id = "transportation_weight";

        DecisionNode beds = new DecisionNode();
        beds.question = "How many beds will your party require?";
        beds.option = NUMBER_PICKER;
        beds.id = "beds";

        DecisionNode rentOrBuy = new DecisionNode();
        rentOrBuy.question = "Would you like to rent or buy a property?";
        rentOrBuy.option = RENT_OR_BUY;
        rentOrBuy.id = "prop_type";

        location.pointers = new DecisionNode[]{age};
        age.pointers = new DecisionNode[]{lessTwentyFive, twentyFiftyFive, overFiftyFive};

        // age option pointers
        lessTwentyFive.pointers = new DecisionNode[]{firstTimeHome};
        twentyFiftyFive.pointers = new DecisionNode[]{categories};
        overFiftyFive.pointers = new DecisionNode[]{access};

        firstTimeHome.pointers = new DecisionNode[]{categories};
        access.pointers = new DecisionNode[]{income};

        // category pointers
        categories.pointers = new DecisionNode[]{singleProfessional, workingIndividual,
                singleParent, modIncomeFamily, duelProfFamily};
        singleProfessional.pointers = new DecisionNode[]{income};
        workingIndividual.pointers = new DecisionNode[]{income};
        singleParent.pointers = new DecisionNode[]{education};
        modIncomeFamily.pointers = new DecisionNode[]{education};
        duelProfFamily.pointers = new DecisionNode[]{education};

        // education pointer
        education.pointers = new DecisionNode[]{income};

        // voucher pointer
        vouchers.pointers = new DecisionNode[]{subsidies};

        subsidies.pointers = new DecisionNode[]{transportationCosts};

        income.pointers = new DecisionNode[]{vouchers};
        transportationCosts.pointers = new DecisionNode[]{beds};

        beds.pointers = new DecisionNode[]{rentOrBuy};
    }

    public void decide(Object option) {
        if (cur.pointers != null) {
            for (int i = 0; i < cur.pointers.length; i++) {
                DecisionNode current = cur.pointers[i];
                if (current.option != null && current.option.equals(option)) {
                    cur = cur.pointers[i]; // traverse down that path.
                }
            }
            if (cur.question == null || hasUpperCase(cur.option)) { // only one path
                cur = cur.pointers[0];
            }
        } else {
            cur = null;
        }
    }

    /**
     * Checks if it has uppercase chars
     */
    private boolean hasUpperCase(String aString) {
        for (char c : aString.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Not a spinner, so just move on to the next node.
     */
    public void decide() {
        cur = cur.pointers[0];
    }

    public DecisionNode getCurrentNode() {
        return cur;
    }


    public class DecisionNode {
        public String question;
        public DecisionNode[] pointers;
        public String option;
        public String id;

        public DecisionNode() {

        }

    }

}
