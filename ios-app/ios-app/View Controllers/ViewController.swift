//
//  ViewController.swift
//  ios-app
//

import UIKit
import JDOCommon

class ViewController: UIViewController {
    
    @IBOutlet weak var userEmailField: UITextField!
    @IBOutlet weak var userPasswordField: UITextField!
    @IBOutlet weak var loginInfoLabel: UILabel!
    
    @IBOutlet weak var loginButton: UIButton!
    
    weak var fulcrumAuthQuery: FulcrumAuthModelQueries!
    
    internal var api = FulcrumApi()

    override func viewDidLoad() {
        super.viewDidLoad()
        
        loginInfoLabel.isHidden = true
        
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
    
    func showAccounts(accounts: Array<Any>) {
        
        let contexts = accounts as! Array<Contexts>
        
        let alert = UIAlertController(title: "Select Organization", message: nil, preferredStyle: .alert)
        
        let closure = { (action: UIAlertAction!) -> Void in
            let index = alert.actions.firstIndex(of: action)
            
            if index != nil {
                self.setAccount(context: contexts[index!])
            }
        }
        
        for context in contexts {
            alert.addAction(UIAlertAction(title: context.name, style: .default, handler: closure))
        }
        
        alert.addAction(UIAlertAction(title: "cancel", style: .cancel, handler: {(_) in }))
        self.present(alert, animated: false, completion: nil)
        
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
    
    func setAccount(context: Contexts) {
        let orgId = context.id
        let organizations = fulcrumAuthQuery.selectUserByOrganizationId(id: orgId).executeAsList()
        
        for organization in organizations {
            let org = organization as! Organizations
            let infos = fulcrumAuthQuery.selectOrgView(id: org.user_id).executeAsList()
            showLoginInfo(infos: infos)
        }
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
        
        if (response.user.contexts.count > 1) {
            showAccounts(accounts: response.user.contexts)
        } else {
            let infos = fulcrumAuthQuery.selectOrgView(id: userID).executeAsList()
            showLoginInfo(infos: infos)
        }
    }
    
    func showLoginInfo(infos: Array<Any>) {
        userEmailField.isHidden = true
        userPasswordField.isHidden = true
        loginButton.isHidden = true

        loginInfoLabel.isHidden = false
        loginInfoLabel.numberOfLines = 20

        var joinText = ""

        for info in infos {
            let join = info as! SelectOrgView
            joinText = "Success! \n\n You are logged in as \(join.first_name) \(join.last_name) in \(join.name) \n\n Your API token is \(join.token)"

        }

        loginInfoLabel.text = joinText
    }
    
    
    internal func handleError(_ error: String?){
        let message = error ?? "An unknown error occurred. Retry?"
        print(message)
        
    }
    
}
