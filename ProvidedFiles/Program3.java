package ProvidedFiles;

//Name: Alexis Torres
//EID: at39625

public class Program3 {

    // DO NOT REMOVE OR MODIFY THESE VARIABLES (calculator and treatment_plan)
    ImpactCalculator calculator; // the impact calculator
    int[] treatment_plan; // array to store the treatment plan

    public Program3() {
        this.calculator = null;
    }

    /*
     * This method is used in lieu of a required constructor signature to initialize
     * your Program3. After calling a default (no-parameter) constructor, we
     * will use this method to initialize your Program3.
     * 
     * DO NOT MODIFY THIS METHOD
     * 
     */
    public void initialize(ImpactCalculator ic) {
        this.calculator = ic;
        this.treatment_plan = new int[ic.getNumMedicines()];
    }

    /*
     * This method computes and returns the total impact of the treatment plan. It
     * should
     * also fill in the treatment_plan array with the correct values.
     * 
     * Each element of the treatment_plan array should contain the number of hours
     * that medicine i should be administered for. For example, if treatment_plan[2]
     * = 5,
     * then medicine 2 should be administered for 5 hours.
     * 
     */

    public int computeImpact() {

        int totalTime = calculator.getTotalTime();
        int numMedicines = calculator.getNumMedicines();
        int[][] OPT = new int[numMedicines + 1][totalTime + 1]; // going to 1 index to account for 0 hours/meds 

        for (int i = 0; i <= numMedicines; i++) { //no hours
            OPT[i][0] = 0;
        }
        for (int i = 0; i <= totalTime; i++) { // no meds
            OPT[0][i] = 0;
        }

        //Building the table

        for (int medicine = 1; medicine <= numMedicines; medicine++) {
            for (int hour = 1; hour <= totalTime; hour++) {
                //Locking for that m value where it becomes greater than the previous value
                for (int dosage_length = 0; dosage_length <= hour; dosage_length++) {
                    int prev_max_impact = OPT[medicine][hour];
                    int impact = calculator.calculateImpact(medicine - 1, dosage_length);
                    int new_potential = OPT[medicine - 1][hour - dosage_length] + impact; //Looking for if swtiching medicines makes impact higher
                    OPT[medicine][hour] = Math.max(prev_max_impact, new_potential); // which ever gives the higher impact value will be the entry

                }
            }
        }

        return OPT[numMedicines][totalTime];
    }

    /*
     * This method prints the treatment plan.
     */
    public void printTreatmentPlan() {
        System.out.println("Please administer medicines 1 through n for the following amounts of time:\n");
        int hoursForI = 0;
        int n = calculator.getNumMedicines();
        for (int i = 0; i < n; i++) {
            // retrieve the amount of hours for medicine i
            hoursForI = treatment_plan[i]; // ... fill in here ...
            System.out.println("Medicine " + i + ": " + hoursForI);
        }
    }
}
