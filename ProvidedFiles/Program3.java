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

    // Function to calculate the peak hour of effectiveness for the the medicines
    private int[] findPeak() {
        int totalTime = calculator.getTotalTime();
        int numMedicines = calculator.getNumMedicines();
        int[] peak_med_hours = new int[numMedicines];

        for (int i = 0; i < numMedicines; i++) {
            peak_med_hours[i] = -1;
        }

        for (int i = 0; i < numMedicines; i++) {
            int prev_value = -1;
            for (int j = 1; j < totalTime + 1; j++) {
                int impact = calculator.calculateImpact(i, j);
                if (prev_value == impact) {
                    peak_med_hours[i] = j - 1; // If i need the index of the hour to be zero indexed then change to 2
                    break;
                }
                prev_value = impact;
            }
            if (peak_med_hours[i] == -1) {
                peak_med_hours[i] = totalTime;
            }
        }

        return peak_med_hours;
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
        int running_impact = 0;

        int[][][] opt2 = new int[numMedicines][totalTime + 1][totalTime + 1];
        treatment_plan = new int[numMedicines];
        boolean[] used = new boolean[numMedicines];
        int[] opt = new int[totalTime + 1];
        // int[] peak_med_hours = findPeak();

        for (int i = 0; i < numMedicines; i++) {
            treatment_plan[i] = 0;
            used[i] = false;
        }
        for (int i = 0; i < totalTime + 1; i++) {
            opt[i] = -1;
        }
        // int highest_first_dose = -1;
        // int hfd_index = -1;
        // for (int med_first_dose = 0; med_first_dose < numMedicines; med_first_dose++)
        // {
        // if (!used[med_first_dose]) {
        // int first_does_impact = calculator.calculateImpact(med_first_dose, 1);
        // if (highest_first_dose < first_does_impact) {
        // highest_first_dose = first_does_impact;
        // hfd_index = med_first_dose;
        // }
        // }
        // }

        int prev_med = -1;
        int prev_med_length = 0;

        for (int hour = 1; hour < totalTime + 1; hour++) {

            int max_impact = 0;
            int highest_first_dose_impact = -1;
            int hfd_index = -1;

            // for (int medicine = 0; medicine < numMedicines; medicine++) {
            // if (peak_med_hours[medicine] >= hour) { // if the medicine has not been maxed
            // // out
            // int impact_change = calculator.calculateImpact(medicine, hour)
            // - calculator.calculateImpact(medicine, hour - 1);

            // if (impact_change > max_impact) {
            // max_impact = impact_change;
            // med_index = medicine;
            // }

            // }

            // }

            for (int med_first_dose = 0; med_first_dose < numMedicines; med_first_dose++) {
                if (!used[med_first_dose]) {
                    int first_does_impact = calculator.calculateImpact(med_first_dose, 1);
                    if (highest_first_dose_impact < first_does_impact) {
                        highest_first_dose_impact = first_does_impact;
                        hfd_index = med_first_dose;
                    }
                }
            }

            // initial case choosing to use the highest impact
            if (prev_med == -1) {
                if (!(hfd_index == -1)) {
                    treatment_plan[hfd_index]++;
                    opt[hour] = hfd_index;
                    used[hfd_index] = true;
                    max_impact = highest_first_dose_impact;
                    prev_med = hfd_index;
                    prev_med_length = 1;
                }

            } else {
                // calculate the impact gain of taking another dose of the previous medicine
                int cont_prev_med_impact = calculator.calculateImpact(prev_med, prev_med_length + 1)
                        - calculator.calculateImpact(prev_med, prev_med_length);
                if (cont_prev_med_impact > highest_first_dose_impact) {
                    prev_med_length++;
                    treatment_plan[prev_med]++;
                    opt[hour] = prev_med;
                    max_impact = cont_prev_med_impact;

                } else {
                    treatment_plan[hfd_index]++;
                    opt[hour] = hfd_index;
                    used[hfd_index] = true;
                    max_impact = highest_first_dose_impact;
                    prev_med = hfd_index;
                    prev_med_length = 1;
                }
            }

            running_impact = running_impact + max_impact;

        }

        // Put your code here

        return running_impact;
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
