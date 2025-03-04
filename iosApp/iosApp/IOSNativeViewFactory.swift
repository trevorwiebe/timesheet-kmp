//
//  IOSNativeViewFactory.swift
//  iosApp
//
//  Created by Trevor Wiebe on 3/3/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp

class IOSNativeViewFactory: NativeViewFactory {
    
    static var shared = IOSNativeViewFactory()

    func createButtonView(text: String, onClick: @escaping () -> Void) -> UIViewController {
        let view = SimpleButton(text: text, action: onClick)
        return UIHostingController(rootView: view)
    }
    
    func createDestructionButton(text: String, onClick: @escaping () -> Void) -> UIViewController {
        let view = DestructiveButton(text: text, action: onClick)
        return UIHostingController(rootView: view)
    }
    
    func createDeletePunchSystem(
        onDelete: @escaping () -> Void
    ) -> UIViewController {
        let view = DeletePunchDialog(onDelete: onDelete)
        return UIHostingController(rootView: view)
    }
}
