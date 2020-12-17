# Pharma-collect mobile app

Pharma collect is a mobile app which allow to send prescription/order to a particular pharmacy and pick out it few moment later into a fully secure box. Pharma collect is by the way cut in two different applications :
- Pharma-collect Client
- Pharma-collect Pro

![](./app/src/main/res/doc/archi.png) 

# Pharma-Collect Client

The client part will allow the user to :
- **Access to a shop**
	- See articles of a pharmacy without being authenticate
	- Get an account and make an order

- **Acces to a private Click&Collect space**
	- Get its informations
	- Send a prescription to a pharmacy
	- Get its localisation and nearby pharamacies
	- Access to the shop
	- Get an historic of prescriptions previously send

## Technologies

The app is made with **Kotlin** and **Java** languages.
We work with fragment and **mvvm** technology (Model-View-ViewModel)

![](./app/src/main/res/doc/mvvm.png)


The application is linked to a server (see previous drawing). We are able to talk with it thanks to an http request (in fact we have **https** request to get safer transactions). In addition, the most sensitive information are encrypted with **BCrypt** technolgy and cannot be violated.

To be connected with the locker, Pharma-collect will use the **QR code technology**. It will allow us to verify the identity of the user in front of the box (informations verified by the locker connected to the server).



## The two different parts

### E-Shop

The shop part of the application will allow a user to see the items (**available without prescription**) that are offered in the pharmacy and possibly its promotions and ads.

The visit of the shop is open to anyone who owns the application. However, the order must be made by being logged in and authenticated.

Possibilities of the E-shop:
- Visit of the shop
- Connection
- Add items to the basket
- Placing an order
- Payment

![](./app/src/main/res/doc/mermaid_shop.png)

An e-shop is also set up on the web to allow users to access the shop of their favourite pharmacies on any platform. It's based on the same operation.

### Click&Collect

The click & Collect part will allow the user to get a private space totally safe with all its information. This part will not be able on other platform!

Furthermore, the user can do lot of actions on this side of the app, such as :
- Create its account
- Get its informations
- Get the position of all nearby pharmacies
- Send a prescription to the pharmacy he wants
- Get an home page which sums up and concentrate all important informations
- Get a direct access to the shop without being disconnected

The location is set thanks to google maps API localisation and google maps API nearby places

![](./app/src/main/res/doc/mermaid_cac.png)

## State of progress

:exclamation: Lets refer to the project board to get more precision :exclamation:

### Common parts

- Animation page / Splash
- Choice menu
- SignUp
- Login

The common part is ended :white_check_mark: 
Only some changes about the design should be done

Extract of the app's common parts :

![](./app/src/main/res/doc/splash.png)
*Animation while enter into the app*
![](./app/src/main/res/doc/choice.png)
*Choice page*
![](./app/src/main/res/doc/login.png)
*Login page*
![](./app/src/main/res/doc/signup.png)
*Signup page*

### E-Shop

:heavy_check_mark: Done :
- Recup / Display of products
- Products description
- Save items in the shopping cart
- Access to the shopping cart
- Menu/navbar working

![](./app/src/main/res/doc/add_two.png)
*Add to shopping cart*
![](./app/src/main/res/doc/add.png)

### Click&Collect

:heavy_check_mark: Done :

- Nearby places
- Nearby pharmacies detail (possibility to show on maps)
- Menu/navbar working

![](./app/src/main/res/doc/nearby.png)
*Nearby places*
![](./app/src/main/res/doc/details.png)
*Place detail*
