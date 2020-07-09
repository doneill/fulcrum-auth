//
//  ViewController.swift
//  ios-app
//

import UIKit
import JDOCommon

class ViewController: UIViewController {
    
    @IBOutlet weak var userEmailField: UITextField!
    @IBOutlet weak var userPasswordField: UITextField!
    
    @IBOutlet weak var loginButton: UIButton!
    
    weak var fulcrumAuthQuery: FulcrumAuthModelQueries!
    
    internal var api = FulcrumApi()

    override func viewDidLoad() {
        super.viewDidLoad()
        
        userEmailField.clearButtonMode = .whileEditing
        
        userPasswordField.isSecureTextEntry.toggle()
        userPasswordField.clearButtonMode = .whileEditing
        
        let driver = FulcrumAuthDriverFactory()
        let db = FulcrumAuthDriverFactoryKt.createDb(fulcrumAuthDriverFactory: driver)
        fulcrumAuthQuery = db.fulcrumAuthModelQueries
    
    }
    
    @IBAction func onClick(_ sender: Any, forEvent event: UIEvent) {
        let user = userEmailField.text!
        let password = userPasswordField.text!
        
        getAccount(user: user, password: password)
    }
    
    func getAccount(user: String, password: String) {
        let loginString = String(format: "%@:%@", user, password)
        let loginData = loginString.data(using: String.Encoding.utf8)!
        let base64LoginString = loginData.base64EncodedString()
        
        api.getAccount(
            authorization: base64LoginString,
            success: { data in
                self.parseAccount(response: data)
        }, failure: {
            self.handleError($0.message)

        })
    }
    
    func parseAccount(response: FulcrumAuthenticationResponse) {
        let userID = response.user.id
        let firstName = response.user.first_name
        let lastName = response.user.last_name
        let email = response.user.email
        
        fulcrumAuthQuery.insertUsers(id: userID, first_name: firstName, last_name: lastName, email: email)
        
        let contexts = response.user.contexts as [Contexts]
        
        for context in contexts {
            let name = context.name
            let orgID = context.id
            let apiToken = context.api_token
            
            fulcrumAuthQuery.insertOrgs(id: orgID, user_id: userID, name: name, token: apiToken)
            
        }
        
        let infos = fulcrumAuthQuery.selectJoinUserOrgByUserId(id: userID).executeAsList()
        
        for info in infos {
            print(info)
        }
        
//        let userData = fulcrumAuthQuery.selectAllUsers().executeAsList()
//        for user in userData {
//            print(user)
//        }
//
//        let orgs = fulcrumAuthQuery.selectAllOrganizations().executeAsList()
//        for org in orgs {
//            print(org)
//        }
        
    }
    
    
    internal func handleError(_ error: String?){
        let message = error ?? "An unknown error occurred. Retry?"
        print(message)
        
    }
    
}
