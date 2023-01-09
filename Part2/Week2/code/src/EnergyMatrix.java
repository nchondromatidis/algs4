/**
 * Created by nikos on 26/4/2015.
 */
public class EnergyMatrix {

    Double[][] energy_array;

    public void energyMatrix(SeamCarver sc)
    {
        energy_array = new Double[sc.width()][sc.height()];
        for (int j = 0; j < sc.height(); j++)
        {
            for (int i = 0; i < sc.width(); i++)
                energy_array[i][j] = sc.energy(i, j);
        }
    }
}
