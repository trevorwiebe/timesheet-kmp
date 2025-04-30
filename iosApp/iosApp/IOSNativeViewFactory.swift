//
//  IOSNativeViewFactory.swift
//  iosApp
//
//  Created by Trevor Wiebe on 4/30/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp

class IOSNativeViewFactory: NativeViewFactory {

    static var shared = IOSNativeViewFactory()
    
    func createTimePickerView(
        punch: Punch,
        onDismiss: @escaping () -> Void,
        onTimeSelected: @escaping (Punch) -> Void
    ) -> UIViewController {
        var host: UIHostingController<TimePicker>? = nil

        let view = TimePicker(
            punch: punch,
            onDismiss: {
                host?.dismiss(animated: true)
                onDismiss()
            },
            onTimeSelected: { punch in
                onTimeSelected(punch)
            }
        )

        let controller = UIHostingController(rootView: view)
        host = controller
        controller.modalPresentationStyle = .overFullScreen
        controller.modalTransitionStyle = .crossDissolve
        controller.view.backgroundColor = .clear

        if let rootVC = UIApplication.shared.windows.first(where: { $0.isKeyWindow })?.rootViewController {
            DispatchQueue.main.async {
                rootVC.present(controller, animated: true, completion: nil)
            }
        }

        return UIViewController()
    }
}
