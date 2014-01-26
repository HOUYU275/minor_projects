package weka.attributeSelection;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;

public class ReliabilityFS
        extends ASSearch
        implements OptionHandler, TechnicalInformationHandler {
	
	private Instances inss;
	private int K;
	
	public ReliabilityFS()
	{
	}
	
	public void setNeighborNumber(int K)
	{
		this.K = K;
	}
	
//	private double R_cell(int instance_index, int attrib_index)
//	{
//		double result = 0;
//		double[] k_nearest_dist = new double[K];
//		double max_dist = 0;
//		int neighbor_count = 0;
//		int max_dist_n_id = -1;
//		double max=0;
//		double min=100000000;
//		
//		for(int i = 0; i<data.length; i++)
//		{
//			//record the max number and the min number of the attribute
//			if(data[i][attrib_index]>max)
//			{
//				max=data[i][attrib_index];
//			}
//			if(data[i][attrib_index]<min)
//			{
//				min=data[i][attrib_index];
//			}
//			
//			if(i==instance_index)
//			{
//				continue;
//			}
//			
//			//when the neighbor number < K, just add all in.
//			if(neighbor_count<K)
//			{				
//				double dist=Math.abs(data[i][attrib_index]-data[instance_index][attrib_index]);
//				k_nearest_dist[neighbor_count] = dist;
//				if(dist>max_dist)
//				{
//					max_dist = dist;
//					max_dist_n_id = neighbor_count;
//				}
//				neighbor_count++;
//				continue;
//			}
//			
//			double dist = Math.abs(data[i][attrib_index]-data[instance_index][attrib_index]);
//			if(dist<max_dist)
//			{
//				k_nearest_dist[max_dist_n_id] = dist;
//				double maxK = 0;
//				int maxid=-1;
//				for(int j=0 ; j<K; j++)
//				{
//					if(k_nearest_dist[j]>maxK)
//					{
//						maxK = k_nearest_dist[j];
//						maxid = j;
//					}
//				}				
//				max_dist = maxK;
//				max_dist_n_id=maxid;
//			}
//			
//		}
//		
//		double sum = 0;
//		for(int i=0; i< K; i++)
//		{
//			sum+=k_nearest_dist[i];
//		}
//		sum = sum/K;
//		result = 1-sum/(max-min);
//		return result;
//	}
	
	private double D_cell(int instance_index, int attrib_index)
	{
		double result = 0;
		double[] k_nearest_dist = new double[K];
		double max_dist = 0;
		int neighbor_count = 0;
		int max_dist_n_id = -1;
		
		for(int i = 0; i<inss.numInstances(); i++)
		{
			if(i==instance_index)
			{
				continue;
			}
			
			//when the neighbor number < K, just add all in.
			if(neighbor_count<K)
			{				
				double dist=Math.abs(inss.instance(i).value(attrib_index)-inss.instance(instance_index).value(attrib_index));
				k_nearest_dist[neighbor_count] = dist;
				if(dist>max_dist)
				{
					max_dist = dist;
					max_dist_n_id = neighbor_count;
				}
				neighbor_count++;
				continue;
			}
			
			double dist = Math.abs(inss.instance(i).value(attrib_index)-inss.instance(instance_index).value(attrib_index));
			if(dist<max_dist)
			{
				k_nearest_dist[max_dist_n_id] = dist;
				double maxK = 0;
				int maxid=-1;
				for(int j=0 ; j<K; j++)
				{
					if(k_nearest_dist[j]>maxK)
					{
						maxK = k_nearest_dist[j];
						maxid = j;
					}
				}				
				max_dist = maxK;
				max_dist_n_id=maxid;
			}
			
		}
		
		double sum = 0;
		for(int i=0; i< K; i++)
		{
			sum+=k_nearest_dist[i];
		}
		result = sum/K;
		return result;
	}
	
	private double R_Attrib(int attrib_index)
	{
		double result = 0;
		double maxValue = 0;
		double minValue = 100000000;
		
		double R_cell[] = new double[inss.numInstances()];
		
		for(int i = 0; i< inss.numInstances(); i++)
		{
			R_cell[i]= D_cell(i,attrib_index);
			if(inss.instance(i).value(attrib_index)>maxValue)
			{
				maxValue=inss.instance(i).value(attrib_index);
			}
			if(inss.instance(i).value(attrib_index)<minValue)
			{
				minValue=inss.instance(i).value(attrib_index);
			}
		}
		if (maxValue == minValue) return  -1;
		for(int i = 0; i<R_cell.length; i++)
		{
			R_cell[i]=R_cell[i]/(maxValue-minValue);
			R_cell[i]=1-R_cell[i];
			result += R_cell[i];
		}
		return result;
	}

	private boolean[] selectedAttrib()
	{
		double[] R_Attrib = new double[inss.numAttributes()-1];
		boolean[] SelectedAttrib = new boolean[inss.numAttributes()-1];
		
		double sum = 0;
		for(int i = 0; i<R_Attrib.length; i++)
		{
			R_Attrib[i] = R_Attrib(i);
		}

        int count = 0;
        for(int i = 0; i<R_Attrib.length; i++)
        {
            if (R_Attrib[i] == -1) SelectedAttrib[i]=false;
            else {
                sum+=R_Attrib[i];
                count++;
            }
        }
		
		double avg = sum/(count);
		for(int i=0; i<R_Attrib.length; i++)
		{
			if(R_Attrib[i]>avg)
			{
				SelectedAttrib[i]=true;
			}
			else 
			{
				SelectedAttrib[i]=false;
			}
		}
		return SelectedAttrib;
	}

    public void setInss(Instances inss) {
        this.inss = inss;
    }

    public static void main(String args[]) throws IOException
	{
		Instances ins;
		Reader file;
		file = new FileReader("C:\\Program Files\\Weka-3-6\\data\\cpu.arff");
		ins = new Instances(file);
        ReliabilityFS fs= new ReliabilityFS();
        fs.setInss(ins);
        fs.setNeighborNumber(1);
		boolean [] result = fs.selectedAttrib();
		for (int i = 0 ; i<ins.numAttributes()-1; i++)
		{
			System.out.println(fs.R_Attrib(i)+",");
		}
		for (int i = 0 ; i<ins.numAttributes()-1; i++)
		{
			System.out.println(result[i]+",");
		}		
	}
    
    public int[] convertAttributes(boolean selected[]) {
        int count = 0;
        for (int i = 0; i < selected.length; i++) {
            if (selected[i] == true) count++;
        }
        int[] selectedAttributes = new int[count];
        count = 0;
        for (int i = 0; i < selected.length; i++) {
            if (selected[i] == true) {
                selectedAttributes[count] = i;
                count++;
            }
        }
        return selectedAttributes;
        
    }

    @Override
    public int[] search(ASEvaluation ASEvaluator, Instances data) throws Exception {
        this.inss = new Instances(data);
        setNeighborNumber(1);
        return convertAttributes(selectedAttrib());
    }

    @Override
    public Enumeration listOptions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setOptions(String[] options) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getOptions() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TechnicalInformation getTechnicalInformation() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
