# Preventing recycling-contamination

## Inspiration
The core idea behind developing this project is that doing wrong recycling hurts more than not doing it at all. As we all know, our planet is going through climatic changes; every individual must somehow contribute towards making a positive change. Waste management is one such huge topic of concern. There are so many individuals who are willing to work towards recycling the waste, but not many of them are aware about the steps involved in an appropriate recycling process. Recycling facilities needs as much contamination-free recycling products/goods as possible. Giving contaminated products such as milk carton which contains milk cannot be recycled, for that user needs to wash it and then put it into the recycling bin. This leads to contamination of other waste products. If every user performs recycling in an appropriate way, we can reduce the efforts at recycling facilities and thereby eventually increase the output of the recycling products. To help mankind and to save our environment, we put forward a small initiative from our end by creating an application which assists the user to recycle the waste in a correct manner.

## What it does
It is an android application which captures an image using the camera and sends it to the IBM Watson cloud. The cloud has various classes to identify the waste and generalize them in one of the class. Based on this generalization, necessary steps are provided to the user in the application which are to be performed in order to recycle the waste in an appropriate manner.

## How we built it
STEPS:
1) Created application in Android Studio.
2) Built Image Classification model using IBM Watson Cloud.
3) Implemented the project based on demo Dataset for different classes.
4) Integration of Watson in Android Studio using API calls and keys.
5) The network was trained and the input images are classified with the help of the built model.

## Challenges we ran into
We understood that simply by image identification, one cannot tell whether the product is recyclable or not for all the cases. We also need insights into the material composition of the product in order to predict accurate results.

## Accomplishments that we're proud of
Although it is a small initiative to help the users to identify correct means of recycling the waste, we think if it even helps a group of individuals to get a better result it will help our ecosystem.
## What we learned
We learned to train the model using IBM Watson cloud and deploy the application using the API and available services. 
