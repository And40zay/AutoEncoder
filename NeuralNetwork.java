import java.util.Random;

public class NeuralNetwork{
    static double sigmoid(double x)
    {
        return 1/(1+ Math.exp(-x));
    }
    static double sigmoidDerivative(double funct)
    {
        return funct * (1-funct);
    }
    static class Neuron{
        double[] w;
        double b;
        double weightedSum;
        double o;
        double delta;

        int numInputs;

        Neuron(int numInputs, Random rand)
        {
            this.numInputs = numInputs;
            w = new double[numInputs];
            for(int i = 0; i<numInputs; i++)
            {
                int[] values = {-1,0,1};
                w[i] = values[rand.nextInt(3)];
            }
            b = 0.0;
        }
        double forward(double[] p)
        {
            weightedSum = b;
            for(int i = 0; i<p.length; i++)
            {
                weightedSum += w[i] * p[i];//taking the inputs
            }//w is a double with weights initalised to {0,1,-1}
            o = sigmoid(weightedSum);
            return o;
        }
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Weights: ");
            for(double weight : w)
            {
                sb.append(weight).append(" ");
            }
            sb.append("Bias: ").append(b);
            return sb.toString();
        }
        public double[] getWeights()
        {
            return w;
        }
        public double getBias()
        {
            return b;
        }
    }
    static class Layer{
        Neuron[] neurons;
        double[] outputs;
        int numNeurons;

        Layer(int numNeurons, int numInputSignals, Random rand)
        {
               this.numNeurons = numNeurons;
               neurons = new Neuron[numNeurons];
               for(int i = 0; i<numNeurons; i++)
               {
                    neurons[i] = new Neuron(numInputSignals, rand);
               }
        }
        double[] forward(double[] inputs)
        {
            outputs = new double[numNeurons];
            for(int i = 0; i<numNeurons; i++)//loops numNeurons (double for each neuron)
            {//goes through outputs[i] finds w*p + b
                outputs[i] = neurons[i].forward(inputs);//sends back to forward in Neuron (w[i] * p[i]) + b, then sigmoid.
            }//inputs becomes p in the forward of Neuron, which is the input to the neuron. Loops over every neuron in the layer and gets the output of each neuron, which is stored in outputs array. Then returns the outputs array, which is used as input to the next layer.
            return outputs;
        }
        public Neuron[] getNeurons()
        {
            return neurons;
        }
        public int getNumNeurons()
        {
            return numNeurons;
        }
    }
    public static class MultiLayerPerceptron{
        public Layer hiddenLayer;
        public Layer outputLayer;

        int inputSize;
        int hiddenSize;
        int outputSize;

        MultiLayerPerceptron(int inputSize, int hiddenSize, int outputSize, Random rand)
        {
            this.inputSize = inputSize;
            this.hiddenSize = hiddenSize;
            this.outputSize = outputSize;

            hiddenLayer = new Layer(hiddenSize, inputSize, rand);
            outputLayer = new Layer(outputSize, hiddenSize, rand);
        }
        double[] forward(double[] p)
        {
            double[] hiddenOutput = hiddenLayer.forward(p);
            return outputLayer.forward(hiddenOutput);//goes to 2nd forward and then 1st. Loops over every neuron in doubles...Sends to "train" forward
        }
        void backwards(double[] d, double[] p, double learningRate)
        {   // d is showing the target output for each output neuron. p is the input to the network, which is the fingerprint. Learning rate is the rate at which we adjust weights and biases.
            double[] hiddenOutput = hiddenLayer.forward(p);
            double[] finalOutput = outputLayer.forward(hiddenOutput);

            double[] delta = new double[outputSize];

            for(int i = 0; i<outputSize; i++)
            {
                delta[i] = ((d[i] - finalOutput[i]) * sigmoidDerivative(finalOutput[i]));
            }
            for(int i = 0; i<outputLayer.numNeurons; i++)
            {
                Neuron neuron = outputLayer.neurons[i];
                for(int j = 0; j<neuron.numInputs; j++)
                {   //hiddenOutput is the output of the hidden layer, which is the input to the output layer. Loops over every input to the output layer and adjusts weights based on delta and learning rate.
                    neuron.w[j] += learningRate * delta[i] * hiddenOutput[j];
                }//10 weights within each output neuron, we are accessing the j-th weight of the i-th output neuron and updating it based on the delta for that output neuron, the learning rate, and the output of the hidden layer for that input. Then we update the bias for that output neuron based on the delta and learning rate.
                neuron.b += learningRate * delta[i];
            }
            double[] hiddenDelta = new double[hiddenSize];
            for(int h = 0; h<hiddenSize; h++)
            {//the output neuron's weight * delta is used to calculate the hidden layer's delta. Loops over every hidden neuron and calculates the delta for that neuron based on the weights from that hidden neuron to the output neurons and the delta of the output neurons, then multiplied by the derivative of the sigmoid function applied to the output of the hidden neuron.
                double sum = 0;
                for(int o = 0; o<outputSize; o++)
                {   //output layer is the layer we just updated, so we can use the new weights to calculate the hidden layer's delta. Loops over every output neuron and calculates the sum of the weights from the hidden neuron to the output neurons multiplied by the delta of the output neurons.
                    sum += outputLayer.neurons[o].w[h] * delta[o];//10 weights (not 8 - HiddenNeuron)
                }//outputLayer.neurons[o] has 10 weights (likely from 10 hidden neurons) and we are accessing the weight that connects the h-th hidden neuron to the o-th output neuron. Then we multiply that weight by the delta of the o-th output neuron and add it to the sum.
                hiddenDelta[h] = sum * sigmoidDerivative(hiddenOutput[h]); //weights from the output neuron from the hidden neuron.
            }//hiddenDelta is the delta for the hidden layer, which is used to update the weights and biases of the hidden layer. Loops over every hidden neuron and calculates the delta for that neuron based on the sum of the weights from that hidden neuron to the output neurons multiplied by the delta of the output neurons, then multiplied by the derivative of the sigmoid function applied to the output of the hidden neuron.
            for(int h = 0; h<hiddenSize; h++)
            {//hiddenLayer.neurons has 10 neurons, we are accessing the h-th neuron in the hidden layer and updating its weights and bias based on the hiddenDelta and learning rate. Loops over every hidden neuron and updates its weights and bias based on the hiddenDelta and learning rate.
                Neuron neuron = hiddenLayer.neurons[h];
                for(int i = 0; i<inputSize; i++)
                {
                    neuron.w[i] += learningRate * hiddenDelta[h] * p[i];
                }// p from the targets[i]!!!. Neuron carries 8 hidden weights! 
                neuron.b += learningRate * hiddenDelta[h];
            }//hidden delta calculated before!  sum * sigmoidDerivative(hiddenOutput[h]) is the hiddenDelta for that hidden neuron, which is used to update the weights and bias of that hidden neuron. Loops over every hidden neuron and updates its weights and bias based on the hiddenDelta and learning rate.
        }
        void train(double[][] inputs, double[][] targets, int epochs, double learningRate)
        {
            for(int epoch = 0; epoch<epochs; epoch++)
            {
                double totalError = 0.0;
                for(int i = 0; i<inputs.length; i++)
                {
                    double[] p = inputs[i];
                    double[] d = targets[i];
                    //p contains the INPUTS IN MAIN on first iteration.
                    double[] actual = forward(p);//contains every neuron
                    //returns outputs of the forward pass, which is the output layer's outputs. Loops over every neuron in output layer and gets the output of each neuron. Then calculates error for each output neuron and adds to total error.
                    double error = 0;
                    //d contains the TARGET OUTPUT in MAIN
                    for(int j = 0; j<outputSize; j++)//d contains first column of targets, which is the target output for each output neuron. Loops over every output neuron and calculates error for each one, then adds to total error.
                    {
                        error += 0.5 * Math.pow((d[j] -actual[j]), 2);
                    }//Actual contains the output of the network for the given input p. Loops over every output neuron and calculates the error for that neuron based on the target output d and the actual output from the network, then adds it to the total error.
                    totalError += error;
                    backwards(d,p,learningRate);

                }
                if(epoch %100 == 0)
                {
                    System.out.println("Epoch: " + epoch + "Total Error: " + totalError);
                }
            }
        }
        int predictClass(double[] p)
        {
            double[] output = forward(p);
            int maxIndex = 0;
            for(int i = 1; i<output.length; i++)
            {
                if(output[i]> output[maxIndex])
                {
                    maxIndex = i;
                }
            }
            return maxIndex;
        }
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Hidden Layer:\n");
            for(Neuron neuron : hiddenLayer.neurons)
            {
                sb.append(neuron.toString()).append("\n");
            }
            sb.append("Output Layer:\n");
            for(Neuron neuron : outputLayer.neurons)
            {
                sb.append(neuron.toString()).append("\n");
            }
            return sb.toString();
        }
        public double[][] getWeights()
        {
            double[][] weights = new double[hiddenSize + outputSize][inputSize + hiddenSize];
            for(int i = 0; i<hiddenSize; i++)
            {
                weights[i] = hiddenLayer.neurons[i].getWeights();
            }
            for(int i = 0; i<outputSize; i++)
            {
                weights[hiddenSize + i] = outputLayer.neurons[i].getWeights();
            }
            return weights;
        }
        public Neuron[] getHiddenNeurons()
        {
            return hiddenLayer.neurons;
        }
        public Neuron[] getOutputNeurons()
        {
            return outputLayer.neurons; 
        }
        public int OutputSize()
        {
            return outputSize;
        }
        public int hiddenSize()
        {
            return hiddenSize;
        }
        public Layer getHiddenLayer()
        {
            return hiddenLayer;
        }
        public Layer getOutputLayer()
        {
            return outputLayer;
        }
        public double[] getHiddenOutput(double[] input)
        {
            return hiddenLayer.forward(input);
        }
        public double[] getOutputFromHidden(double[] hiddenOutput)
        {
            return outputLayer.forward(hiddenOutput);
        }
}
}
/*





*/