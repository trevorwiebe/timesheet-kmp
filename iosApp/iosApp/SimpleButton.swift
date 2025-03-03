//
//  Button.swift
//  iosApp
//
//  Created by Trevor Wiebe on 3/3/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp

class IOSNativeViewFactory: NativeViewFactory {
    static var shared = IOSNativeViewFactory()
    func createButtonView(text: String, onClick: @escaping () -> Void) -> UIViewController{
        let view = SimpleButton(text: text, action: onClick)
        return UIHostingController(rootView: view)
    }
}

struct SimpleButton: View {
    var text: String
    var action: () -> Void
    
    var body: some View{
        Button(
            action: action,
            label: {
                Text(text).font(.headline)
            }
        )
    }
}
