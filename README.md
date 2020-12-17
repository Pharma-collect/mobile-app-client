# Pharma-collect mobile app

![](./app/src/main/res/drawable/my_pharma.png)

Pharma collect is a mobile app which allow to send prescription/order to a particular pharmacy and pick out it few moment later into a fully secure box. Pharma collect is by the way cut in two different applications :
- Pharma-collect Client
- Pharma-collect Pro

![Architecture of Pharma-collect mobile](./app/src/main/res/doc/archi.png)


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

The app is made with Kotlin and Java languages.
We work with fragment and mvvm technology (Model-View-ViewModel)

![](./app/src/main/res/doc/mvvm.png)


The application is connected to a server (see previous drawing). The application is connected to it by means of an http request (in fact https to obtain more secure transactions). In addition, the application and the most sensitive information are encrypted and cannot be violated.

To be connected with the locker, Pharma-collect will use the QR code technology. It will allow us to verify the identity of the user in front of the box (informations verified by the locker connected to the server).



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

```mermaid
graph LR
A[App's home page] --> B((Shop))
C{Sign up} --> K
K --> C
B --> L[Visit of the shop]
B --> D[Visit of the shop]
K --> F[Place orders]
D --> K{Auth}
```

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

```mermaid
graph LR
A[App's home page] --> B{Login}
B --> I[Sign up]
I --> B
B --> C((C&Co))
C --> D[Profile page]
C --> E[Nearby pharmacies]
C --> F[Send a prescription]
C --> G[History of prescription]
C --> H[Access to the shop already auth]
```

## State of progress

Lets refer to the project board to get more precision

### Common parts

Done :
- Animation page / Splash
- Choice menu
- SignUp
- Login

The common part is ended even if some design modification have to be done

Extract of the app's common parts :

![Animation while enter into the app](./app/src/main/res/doc/splash.png)
![Choice page](./app/src/main/res/doc/choice.png)
![Login page](./app/src/main/res/doc/login.png)
![Signup page](./app/src/main/res/doc/signup.png)

### E-Shop

Done :
- Recup / Display of products
- Products description
- Save items in the shopping cart
- Access to the shopping cart
- Menu/navbar working

![Add to shopping cart](./app/src/main/res/doc/add_two.png)
![](./app/src/main/res/doc/add.png)

### Click&Collect

Done :

- Nearby places
- Nearby pharmacies detail (possibility to show on maps)
- Menu/navbar working

![Nearby places](./app/src/main/res/doc/nearby.png)
![Place detail](./app/src/main/res/doc/details.png)
