#include "Neuron.h"
//#include <cstdlib>
#include <iostream>
#include <time.h>
#include <math.h> 
#include <stdlib.h>

Neuron::Neuron(int inDegree) {
    double neuronWeights[inDegree];
    for(int i = 0; i < inDegree; i++) {
        neuronWeights[i] = getRandomWeight();
    }
    weights = neuronWeights;
}

void Neuron::calculateOutput(layer previousLayer){    
    double sum = 0.0;
    for(int i = 0; i < previousLayer.size(); i++) {
        sum += previousLayer[i].outputValue*weights[i];   
    }
    //do transfer over sum
    this->outputValue = transferFunction(sum);
}

double Neuron::transferFunction(double d) {
    //return (d/(1 + abs(d))); sigmoid
    return tanh(d);
}

void Neuron::calculateOutputDelta(double targetValue) {
    double diff = targetValue - outputValue;
    this->delta = diff*(1 - outputValue*outputValue);
}

void Neuron::calculateHiddenDelta(layer& nextLayer, int index) {
    double sumNextLayer = 0.0;
    for(int i = 0; i < nextLayer.size(); i++) {
        sumNextLayer += nextLayer[i].weights[index]*nextLayer[i].delta;
    }
    //this->delta = (outputValue)*(1-outputValue)*sumNextLayer; (sigmoid derivative). 
    this->delta = (1 - outputValue*outputValue)*sumNextLayer;
}

void Neuron::updateWeight(layer& previousLayer) {
    for(int i = 0; i < previousLayer.size(); i++) {
        Neuron& n = previousLayer[i];
        double change = learningRate * delta * n.outputValue;
        this->weights[i] += change;
    }
}

double Neuron::getRandomWeight() {
    return (rand()/double(RAND_MAX));
}

double Neuron::learningRate = 0.4; //arbitrary for now. 